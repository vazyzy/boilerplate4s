package shoppinglist.infrastructure

import shoppinglist.api.{ListItemId, ShoppingListManagementError}
import shoppinglist.domain.ListItem
import shoppinglist.spi.ShoppingListItemRepo
import zio.{URIO, ZIO}

final class DefaultShoppingListItemRepo(dao: MysqlShoppingListItemDao)  extends ShoppingListItemRepo {
  override def modify[R](id: ListItemId)(f: Option[ListItem] => URIO[R, Option[ListItem]]): ZIO[R, ShoppingListManagementError, Unit] =
    dao.
}
