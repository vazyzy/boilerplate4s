package shoppinglist.domain

import shoppinglist.api.{ListId, ListItemId, OwnerId}
import zio.{UIO, ZIO}

final case class ShoppingList(
    id: ListId,
    ownerId: OwnerId,
    name: String
) {
  def addItem(itemId: ListItemId, name: String, quantity: Int): ListItem =
    ListItem(itemId, id, name, quantity)
}
case class ListItem(id: ListItemId, listId: ListId, name: String, quantity: Int) {
  def rename(newName: String): ListItem =
    if (name == newName)
      this
    else
      copy(name = newName)

  def changeQuantity(newQuantity: Int): ListItem =
    if (newQuantity == quantity)
      this
    else
      copy(quantity = newQuantity)

}

object ListId {
  def generate: UIO[ListId] = ZIO.randomWith(_.nextUUID).map(_.toString)
}
object ListItemId {
  def generate: UIO[ListItemId] = ZIO.randomWith(_.nextUUID).map(_.toString)
}
