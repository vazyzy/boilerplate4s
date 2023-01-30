package shoppinglist.domain

import shoppinglist.api.{ListId, ListItemId, OwnerId, ShoppingListManagementError}
import ShoppingListManagementError._
import zio.{UIO, ZIO}

final case class ShoppingList(
    id: ListId,
    ownerId: OwnerId,
    name: String,
    items: List[ListItem]
) {

  def addItem(itemId: ListItemId, name: String, quantity: Int): Either[ShoppingListManagementError, ShoppingList] =
    if (items.exists(_.id == itemId))
      Left(DuplicateItemId)
    else if (items.exists(_.name == name))
      Left(DuplicateItemName)
    else
      Right(
        copy(items = ListItem(itemId, name, quantity) :: items)
      )

  def removeItem(itemId: ListItemId): Either[ShoppingListManagementError, ShoppingList] = {
    val (item, otherItems) = items.partition(_.id == itemId)
    if (item.isEmpty)
      Left(ItemNotExist)
    else
      Right(copy(items = otherItems))
  }

  def renameItem(itemId: ListItemId, newName: String): Either[ShoppingListManagementError, ShoppingList] =
    if (items.exists(i => i.id != itemId && i.name == newName))
      Left(DuplicateItemName)
    else {
      val (item, otherItems) = items.partition(_.id == itemId)
      item.headOption match {
        case None => Left(ItemNotExist)
        case Some(item) =>
          if (item.name == newName)
            Right(this)
          else
            Right(copy(items = item.copy(name = newName) :: otherItems))
      }
    }

  def changeItemQuantity(itemId: ListItemId, newQuantity: Int): Either[ShoppingListManagementError, ShoppingList] = {
    val (item, otherItems) = items.partition(_.id == itemId)
    item.headOption match {
      case None       => Left(ItemNotExist)
      case Some(item) => Right(copy(items = item.copy(quantity = newQuantity) :: otherItems))
    }
  }
}
object ShoppingList {
  def empty(id: ListId, ownerId: OwnerId, name: String): ShoppingList =
    ShoppingList(id, ownerId, name, List.empty)
}
case class ListItem(id: ListItemId, name: String, quantity: Int)

object ListId {
  def generate: UIO[ListId] = ZIO.randomWith(_.nextUUID).map(_.toString)
}
object ListItemId {
  def generate: UIO[ListItemId] = ZIO.randomWith(_.nextUUID).map(_.toString)
}
