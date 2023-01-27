package shoppinglist.api

case class AddItem(name: String, price: Double, quantity: Int)

case class UpdateItem(name: String, price: Double, quantity: Int)

case class ListItem(id: ItemId, name: String, price: Double, quantity: Int)

type ItemId = String
type ListId = String
type OwnerId = String

trait ShoppingListManagementError

object ShoppingListManagementError {

  object ItemNotExist extends ShoppingListManagementError

  object ListNotExist extends ShoppingListManagementError
}

// Use case can be defined as a collection,
// entity which aggregates other entities and plays role of the factory for children elements
trait ShoppingListManagement[F[_]] {
  def createList(ownerId: OwnerId): F[ListId]

  def addItem(ownerId: OwnerId, listId: ListId, item: AddItem): F[Unit]

  def removeItem(ownerId: OwnerId, listId: ListId, item: ItemId): F[Either[Unit, ShoppingListManagementError]]

  def updateItem(ownerId: OwnerId, listId: ListId, item: ItemId, newItem: UpdateItem): F[Either[ListItem, ShoppingListManagementError]]

  def getItems(ownerId: OwnerId, listId: ListId): F[Either[List[ListItem], ShoppingListManagementError]]
}



