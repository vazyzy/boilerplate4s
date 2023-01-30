package shoppinglist.spi

import shoppinglist.api._
import shoppinglist.domain.ShoppingList
import zio.ZIO

trait ShoppingListRepo {

  def access[R, E](id: ListId)(f: Option[ShoppingList] => ZIO[R, E, Option[ShoppingList]]): ZIO[R, E, Unit]

}
