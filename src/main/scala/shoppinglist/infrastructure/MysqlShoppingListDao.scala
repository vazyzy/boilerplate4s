package shoppinglist.infrastructure

import shoppinglist.api._
import shoppinglist.infrastructure.MysqlShoppingListDao._


//Generated boilerplate
class MysqlShoppingListDao[F[_]] {

   def createList(ownerId: OwnerId): F[ListId] = ???

   def addItem(ownerId: OwnerId,
               listId: ListId,
               item: AddItemDocument): F[Unit] = ???

   def removeItem(ownerId: OwnerId,
                  listId: ListId,
                  item: ItemId): F[Either[Unit, MysqlShoppingListDaoError]] = ???

   def updateItem(ownerId: OwnerId,
                  listId: ListId,
                  item: ItemId,
                  newItem: UpdateItemDocument): F[Either[ListItemDocument, MysqlShoppingListDaoError]] = ???

   def getItems(ownerId: OwnerId,
                listId: ListId): F[Either[List[ListItem], MysqlShoppingListDaoError]] = ???
}


//Generated boilerplate
object MysqlShoppingListDao {

   case class AddItemDocument(name: String, price: Double, quantity: Int)

   case class UpdateItemDocument(name: String, price: Double, quantity: Int)

   case class ListItemDocument(id: ItemId, name: String, price: Double, quantity: Int)

   sealed trait MysqlShoppingListDaoError

   object MysqlShoppingListDaoError {

      object ItemNotFound extends MysqlShoppingListDaoError

      object ListNotFound extends MysqlShoppingListDaoError
   }
}