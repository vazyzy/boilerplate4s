package shoppinglist.api

import zio.{IO, UIO}

case class ListItemView(id: ListItemId, name: String, quantity: Int)
case class ShoppingListView(
    id: ListId,
    ownerId: OwnerId,
    items: List[ListItemView]
)

sealed trait ShoppingListManagementError

object ShoppingListManagementError {

  object ItemNotExist extends ShoppingListManagementError
  object DuplicateItemName extends ShoppingListManagementError
  object DuplicateItemId extends ShoppingListManagementError

  object ListNotExist extends ShoppingListManagementError
}

// Use case can be defined as a collection,
// entity which aggregates other entities and plays role of the factory for children elements
trait ShoppingListManagement {
  def createList(ownerId: OwnerId, name: String): UIO[ListId]

  def addItem(
      // This should be renamed to userId and used for authorization purposes,
      // otherwise there's no need for passing this parameter.
      ownerId: OwnerId,
      listId: ListId,
      name: String,
      quantity: Int
  ): IO[ShoppingListManagementError, ListItemId]

  def removeItem(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId
  ): IO[ShoppingListManagementError, Unit]

  def renameItem(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId,
      newName: String
  ): IO[ShoppingListManagementError, Unit]

  def changeItemQuantity(
      ownerId: OwnerId,
      listId: ListId,
      itemId: ListItemId,
      newQuantity: Int
  ): IO[ShoppingListManagementError, Unit]

  def getList(
      ownerId: OwnerId,
      listId: ListId
  ): IO[ShoppingListManagementError, ShoppingListView]

  def deleteList(
      ownerId: OwnerId,
      listId: ListId
  ): IO[ShoppingListManagementError, Unit]
}
