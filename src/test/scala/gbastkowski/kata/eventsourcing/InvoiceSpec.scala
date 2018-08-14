package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    val coke = InvoiceItem(1, "Coke", 1)
    val fries = InvoiceItem(2, "Fries", 2)

    "which is new and empty" - {
      def invoice = Invoice.create(1)

      "can add an item" in {
        invoice.addItem(coke).items should have size 1
      }

      "can change it's recipient" in {
        val tested = invoice.changeRecipient(true)
        tested.recipient shouldBe true
      }

      "can be sent" in {
        an[IllegalArgumentException] should be thrownBy invoice.send
      }
    }

    "with two items" - {
      def invoice = Invoice.create(1).addItem(coke).addItem(fries)

      "can remove an item" in {
       invoice.removeItem(coke.id).items should have size(1)
      }

      "has a correct totalAmount" in {
        invoice.totalAmount shouldBe 3
      }
    }
  }
}
