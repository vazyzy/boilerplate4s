package shoppinglist.spi

import shoppinglist.api.{ListItemId, ShoppingListManagementError}
import shoppinglist.domain.ListItem
import zio.{URIO, ZIO}

trait ShoppingListItemRepo {
  def modify[R](id: ListItemId)(
      f: Option[ListItem] => URIO[R, Option[ListItem]]
  ): ZIO[R, ShoppingListManagementError, Unit]
}
