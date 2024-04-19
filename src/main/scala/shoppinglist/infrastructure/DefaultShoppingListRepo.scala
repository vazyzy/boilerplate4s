package shoppinglist.infrastructure

import shoppinglist.api._
import shoppinglist.domain.{ListItem, ShoppingList}
import shoppinglist.spi.ShoppingListRepo
import MysqlShoppingListDao.ShoppingListRow
import MysqlShoppingListItemDao.ShoppingListItemRow
import zio.{UIO, ZIO}

//Generated boilerplate
final class DefaultShoppingListRepo(
    lists: MysqlShoppingListDao,
    listItems: MysqlShoppingListItemDao
) extends ShoppingListRepo {

  override def access[R, E, A](id: ListId)(f: Option[ShoppingList] => ZIO[R, E, A]): ZIO[R, E, A] =
    load(id).flatMap(f)

  override def modify[R, E](id: ListId)(
      f: Option[ShoppingList] => ZIO[R, E, Option[ShoppingList]]
  ): ZIO[R, E, Unit] =
    load(id)
      .flatMap { in =>
        f(in).flatMap { out =>
          out
            .fold(delete(id))(upsert(id))
            .when(out != in)
            .unit
        }
      }

  private def load(listId: ListId): UIO[Option[ShoppingList]] =
    lists
      .find(listId)
      .some
      .flatMap { listRow =>
        listItems.findWithListId(listRow.id).map { itemRows =>
          fromRows(listRow, itemRows)
        }
      }
      .option

  private def fromRows(
      listRow: ShoppingListRow,
      itemRows: List[ShoppingListItemRow]
  ): ShoppingList =
    ShoppingList(
      ownerId = listRow.ownerId,
      name = listRow.name,
      items = itemRows.map { case ShoppingListItemRow(itemId, name, quantity) =>
        ListItem(
          itemId,
          name,
          quantity
        )
      }
    )

  private def upsert(listId: ListId)(list: ShoppingList): UIO[Unit] = {
    val (listRow, listItemRows) = toRows(listId, list)
    lists.upsert(listRow) *> listItems.upsert(listId, listItemRows)
  }

  private def toRows(
      listId: ListId,
      list: ShoppingList
  ): (ShoppingListRow, List[ShoppingListItemRow]) = {
    val listRow = ShoppingListRow(
      listId,
      list.ownerId,
      list.name
    )
    val listItemRows = list.items.map { case ListItem(id, name, quantity) =>
      ShoppingListItemRow(id, name, quantity)
    }
    (listRow, listItemRows)
  }

  private def delete(listId: ListId): UIO[Unit] = {
    lists.delete(listId) *> listItems.deleteWithListId(listId)
  }
}
