package shoppinglist.domain

import shoppinglist.api._
import shoppinglist.spi.ShoppingListStorage

// Generated boilerplate
class DefaultShoppingListManagement[F[_]](storage: ShoppingListStorage[F]) extends ShoppingListManagement[F] {

  def createList(ownerId: OwnerId): F[ListId] =
    storage.createList(ownerId)

  def addItem(ownerId: OwnerId,
              listId: ListId,
              item: AddItem): F[Unit] =
    storage.addItem(ownerId, listId, item)

  def removeItem(ownerId: OwnerId,
                 listId: ListId,
                 item: ItemId):
  F[Either[Unit, ShoppingListManagementError]] =
    storage.removeItem(ownerId, listId, item)


  def updateItem(ownerId: OwnerId,
                 listId: ListId,
                 item: ItemId,
                 newItem: UpdateItem): F[Either[ListItem, ShoppingListManagementError]] =
    storage.updateItem(ownerId, listId, item, newItem)


  def getItems(ownerId: OwnerId,
               listId: ListId): F[Either[List[ListItem], ShoppingListManagementError]] =
    storage.getItems(ownerId, listId)

}



