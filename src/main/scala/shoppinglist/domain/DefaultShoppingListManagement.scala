package shoppinglist.domain

import shoppinglist.api._
import shoppinglist.spi.{ShoppingListItemRepo, ShoppingListRepo}
import zio.{IO, UIO, ZIO}

// Generated boilerplate
final class DefaultShoppingListManagement(lists: ShoppingListRepo, items: ShoppingListItemRepo)
    extends ShoppingListManagement {

  override def createList(ownerId: OwnerId, name: String): UIO[ListId] =
    ListId.generate
      .tap { listId =>
        lists.modify(listId) {
          case None       => ZIO.some(ShoppingList(listId, ownerId, name))
          case Some(list) => ZIO.fail(list)
        }
      }
      .catchAll(_ => createList(ownerId, name))

  override def addItem(
      ownerId: OwnerId,
      listId: ListId,
      name: String,
      quantity: Int
  ): IO[ShoppingListManagementError, ListItemId] =
    lists.access(listId) {
      case None => ZIO.fail(ShoppingListManagementError.ListNotExist)
      case Some(list) =>
        ListItemId.generate.tap { itemId =>
          items.modify(itemId) {
            case Some(_) => ZIO.dieMessage("Duplicate Generated Item Id")
            case None    => ZIO.some(list.addItem(itemId, name, quantity))
          }
        }
    }

  override def removeItem(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId
  ): IO[ShoppingListManagementError, Unit] =
    items.modify(itemId) {
      case None | Some(item) if item.listId != listId => ZIO.fail(ShoppingListManagementError.ItemNotExist)
      case Some(_)                                    => ZIO.none
    }

  override def renameItem(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId,
      newName: String
  ): IO[ShoppingListManagementError, Unit] =
    items.modify(itemId) {
      case None | Some(item) if item.listId != listId => ZIO.fail(ShoppingListManagementError.ItemNotExist)
      case Some(item)                                 => ZIO.some(item.rename(newName))
    }

  override def changeItemQuantity(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId,
      newQuantity: Int
  ): IO[ShoppingListManagementError, Unit] =
    items.modify(itemId) {
      case None | Some(item) if item.listId != listId => ZIO.fail(ShoppingListManagementError.ItemNotExist)
      case Some(item)                                 => ZIO.some(item.changeQuantity(newQuantity))
    }

  override def deleteList(ownerId: OwnerId, listId: ListId): IO[ShoppingListManagementError, Unit] =
    lists.modify(listId) {
      case None    => ZIO.fail(ShoppingListManagementError.ListNotExist)
      case Some(_) => ZIO.none
    }

  override def getList(
      ownerId: OwnerId,
      listId: ListId
  ): IO[ShoppingListManagementError, ShoppingListView] = ???

}
