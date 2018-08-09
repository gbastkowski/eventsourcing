package gbastkowski.kata.eventsourcing

import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    "which is new and empty" - {
      val invoice = new Invoice()

      "can add an item" in {
        invoice.addItem("Coke", 1)
        invoice.items should have size 1
      }

      "can remove an item" in {
        invoice.removeItem(invoice.items(0))
        invoice.items shouldBe empty
      }

      "can change it's recipient" in {
        invoice.changeRecipient(Some("Recipient"))
      }

      "can be sent" in {
        invoice.send()
        invoice.sent shouldBe defined
      }

      "can be reminded" in {
        invoice.remind()
        invoice.reminded shouldBe defined
      }
    }

    "which has been sent" - {
      val invoice = new Invoice()
      invoice.addItem("coke", 2)
      invoice.send()

      "cannot add items" in {
        an[IllegalArgumentException] should be thrownBy invoice.addItem("a", 1)
      }

      "cannot remove items" in {
        an[IllegalArgumentException] should be thrownBy invoice.removeItem(invoice.items(0))
      }
    }
  }
}
