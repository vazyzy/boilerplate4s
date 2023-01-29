package shoppinglist.domain

import shoppinglist.api.ShoppingListManagementError._
import zio.test.Assertion._
import zio.test._
object ShoppingListSpec extends ZIOSpecDefault {
  def spec =
    suite("HelloWorldSpec")(
      test("ShoppingList.empty") {
        val emptyList = ShoppingList.empty("", "", "")
        assertTrue(emptyList.items.isEmpty)
      },
      test("ShoppingList#addItem") {
        val emptyList = ShoppingList.empty("", "", "")
        val addResult = emptyList.addItem("1", "name", 1)
        val addWithSameIdResult = addResult.flatMap(_.addItem("1", "other name", 1))
        val addWithSameNameResult = addResult.flatMap(_.addItem("2", "name", 1))
        assert(addResult)(isRight(hasField("items", _.items, contains(ListItem("1", "name", 1))))) &&
        assert(addWithSameIdResult)(isLeft(equalTo(DuplicateItemId))) &&
        assert(addWithSameNameResult)(isLeft(equalTo(DuplicateItemName)))
      },
      test("ShoppingList#removeItem") {
        val emptyList = ShoppingList.empty("", "", "")
        val Right(list) = emptyList.addItem("1", "name", 1)
        val removeExistingItemResult = list.removeItem("1")
        val removeMissingIdResult = list.removeItem("2")
        assert(removeExistingItemResult)(isRight(hasField("items", _.items, isEmpty))) &&
        assert(removeMissingIdResult)(isLeft(equalTo(ItemNotExist)))
      },
      test("ShoppingList#renameItem") {
        val Right(list) = ShoppingList
          .empty("", "", "")
          .addItem("1", "name", 1)
        val renameExistingItemResult = list.renameItem("1", "newName")
        val renameMissingItem = list.renameItem("2", "newName")
        val renameToExistingName = list
          .addItem("2", "newName", 2)
          .flatMap(_.renameItem("1", "newName"))
        assert(renameExistingItemResult)(
          isRight(
            assertion("item.name")(
              _.items.headOption.map(_.name).contains("newName")
            )
          )
        ) &&
        assert(renameMissingItem)(isLeft(equalTo(ItemNotExist))) &&
        assert(renameToExistingName)(isLeft(equalTo(DuplicateItemName)))
      },
      test("ShoppingList#changeItemQuantity") {
        val Right(list) = ShoppingList
          .empty("", "", "")
          .addItem("1", "name", 1)
        val changeQuantityOfExistingItemResult = list.changeItemQuantity("1", 2)
        val changeQuantityOfMissingItemResult = list.changeItemQuantity("2", 3)
        assert(changeQuantityOfExistingItemResult)(
          isRight(
            assertion("item.quantity")(
              _.items.headOption.map(_.quantity).contains(2)
            )
          )
        ) &&
        assert(changeQuantityOfMissingItemResult)(isLeft(equalTo(ItemNotExist)))
      }
    )
}
