package gbastkowski.kata.eventsourcing

import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    val item = InvoiceItem("Coke", 1)

    "which is new and empty" - {
      val invoice = new Invoice()

      "can add an item" in {
        invoice.addItem(item).items should have size 1
      }

      "can remove an item" in {
        invoice.addItem(item).removeItem(item).items shouldBe empty
      }

      "can change it's recipient" in {
        invoice.changeRecipient(Some("Recipient")).recipient shouldBe Some("Recipient")
      }

      "can be sent" in {
        invoice.send().sent shouldBe defined
      }

      "can be reminded" in {
        invoice.remind().reminded shouldBe defined
      }
    }

    "which has been sent" - {
      val invoice = new Invoice().addItem(item).send()

      "cannot add items" in {
        an[IllegalArgumentException] should be thrownBy invoice.addItem(item)
      }

      "cannot remove items" in {
        an[IllegalArgumentException] should be thrownBy invoice.removeItem(invoice.items(0))
      }
    }
  }
}
