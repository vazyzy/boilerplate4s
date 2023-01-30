package shoppinglist.domain

import shoppinglist.api._
import shoppinglist.spi.ShoppingListRepo
import zio.{IO, UIO, ZIO}

// Generated boilerplate
final class DefaultShoppingListManagement(repo: ShoppingListRepo) extends ShoppingListManagement {

  override def createList(ownerId: OwnerId, name: String): UIO[ListId] =
    ListId.generate.tap { listId =>
      repo.access(listId) {
        case None    => ZIO.some(ShoppingList.empty(ownerId, name))
        case Some(_) => ZIO.dieMessage(s"Duplicate list identifier ${listId}")
      }
    }

  override def addItem(
      ownerId: OwnerId,
      listId: ListId,
      name: String,
      quantity: Int
  ): IO[ShoppingListManagementError, ListItemId] =
    ListItemId.generate.tap { itemId =>
      repo.access(listId) {
        case None       => ZIO.fail(ShoppingListManagementError.ListNotExist)
        case Some(list) => ZIO.from(list.addItem(itemId, name, quantity)).asSome
      }
    }

  override def removeItem(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId
  ): IO[ShoppingListManagementError, Unit] =
    repo.access(listId) {
      case None       => ZIO.fail(ShoppingListManagementError.ListNotExist)
      case Some(list) => ZIO.from(list.removeItem(itemId)).asSome
    }

  override def renameItem(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId,
      newName: String
  ): IO[ShoppingListManagementError, Unit] =
    repo.access(listId) {
      case None       => ZIO.fail(ShoppingListManagementError.ListNotExist)
      case Some(list) => ZIO.from(list.renameItem(itemId, newName)).asSome
    }

  override def changeItemQuantity(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId,
      newQuantity: Int
  ): IO[ShoppingListManagementError, Unit] =
    repo.access(listId) {
      case None       => ZIO.fail(ShoppingListManagementError.ListNotExist)
      case Some(list) => ZIO.from(list.changeItemQuantity(itemId, newQuantity)).asSome
    }

  override def deleteList(ownerId: OwnerId, listId: ListId): IO[ShoppingListManagementError, Unit] =
    repo.access(listId) {
      case None    => ZIO.fail(ShoppingListManagementError.ListNotExist)
      case Some(_) => ZIO.none
    }

  override def getList(
      ownerId: OwnerId,
      listId: ListId
  ): IO[ShoppingListManagementError, ShoppingListView] = ???

}
