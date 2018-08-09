package gbastkowski.kata.eventsourcing

import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    "which is new and empty" - {
      val invoice = new Invoice()

      "can add an item" in {
        invoice.addItem("Coke", 1).items should have size 1
      }

      "can remove an item" in {
        invoice.removeItem(invoice.items(0)).items shouldBe empty
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
      val invoice = new Invoice().
        addItem("coke", 2).
        send()

      "cannot add items" in {
        an[IllegalArgumentException] should be thrownBy invoice.addItem("a", 1)
      }

      "cannot remove items" in {
        an[IllegalArgumentException] should be thrownBy invoice.removeItem(invoice.items(0))
      }
    }
  }
}
