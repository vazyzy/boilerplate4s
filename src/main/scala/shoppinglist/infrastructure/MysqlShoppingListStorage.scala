package shoppinglist.infrastructure

import shoppinglist.api._
import shoppinglist.spi.ShoppingListStorage

//Generated boilerplate
class MysqlShoppingListStorage[F[_]] extends ShoppingListStorage[F] {

  override def createList(ownerId: OwnerId): F[ListId] = ???

  override def addItem(ownerId: OwnerId, listId: ListId, item: AddItem): F[Unit] = ???

  override def removeItem(ownerId: OwnerId,
                          listId: ListId,
                          item: ItemId): F[Either[Unit, ShoppingListManagementError]] = ???

  override def updateItem(ownerId: OwnerId,
                          listId: ListId,
                          item: ItemId,
                          newItem: UpdateItem): F[Either[ListItem, ShoppingListManagementError]] = ???

  override def getItems(ownerId: OwnerId,
                        listId: ListId): F[Either[List[ListItem], ShoppingListManagementError]] = ???
}



