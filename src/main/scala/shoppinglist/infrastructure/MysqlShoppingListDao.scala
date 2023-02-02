package shoppinglist.infrastructure

import shoppinglist.api._
import shoppinglist.infrastructure.MysqlShoppingListDao.ShoppingListRow
import shoppinglist.infrastructure.MysqlShoppingListItemDao.ShoppingListItemRow
import zio.UIO

//Generated boilerplate
final class MysqlShoppingListDao {
  def find(listId: ListId): UIO[Option[ShoppingListRow]] = ???
  def delete(listId: ListId): UIO[Unit] = ???

  def upsert(row: ShoppingListRow): UIO[Unit] = ???
}
//Generated boilerplate
object MysqlShoppingListDao {

  case class ShoppingListRow(id: ListId, ownerId: OwnerId, name: String)
}

class MysqlShoppingListItemDao {
  def findWithListId(listId: ListId): UIO[List[ShoppingListItemRow]] = ???
  def deleteWithListId(listId: ListId): UIO[Unit] = ???
  def upsert(listId: ListId, rows: List[ShoppingListItemRow]): UIO[Unit] = ???
}

object MysqlShoppingListItemDao {

  case class ShoppingListItemRow(
      id: ListItemId,
      name: String,
      quantity: Int
  )
}
