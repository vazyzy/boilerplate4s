package shoppinglist.spi

import shoppinglist.api._


//Generated boilerplate
trait ShoppingListRepo[F[_]] {

  def createList(ownerId: OwnerId): F[ListId]

  def addItem(ownerId: OwnerId, listId: ListId, item: AddItem): F[Unit]

  def removeItem(ownerId: OwnerId, listId: ListId, item: ItemId): F[Either[Unit, ShoppingListManagementError]]

  def updateItem(ownerId: OwnerId, listId: ListId, item: ItemId, newItem: UpdateItem): F[Either[ListItem, ShoppingListManagementError]]

  def getItems(ownerId: OwnerId, listId: ListId): F[Either[List[ListItem], ShoppingListManagementError]]
}



