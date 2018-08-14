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
        val tested = invoice.send().sent shouldBe true
      }

      "cannot be reminded" in {
        an[IllegalArgumentException] should be thrownBy invoice.remind()
      }

      "cannot receive payments" in {
        an[IllegalArgumentException] should be thrownBy invoice.receivePayment(LocalDateTime.now())
      }
    }

    "with two items" - {
      def invoice = Invoice.create(1).addItems(coke, fries)

      "can remove an item" in {
       invoice.removeItem(coke.id).items should have size(1)
      }

      "has a correct totalAmount" in {
        invoice.totalAmount shouldBe 3
      }
    }

    "which has been sent" - {
      val tested = Invoice.create(1).send()
      // tested.addItem(coke)

      "cannot add items" in {
        an[IllegalArgumentException] should be thrownBy tested.addItem(coke)
      }

      "cannot remove items" in {
        an[IllegalArgumentException] should be thrownBy tested.removeItem(0)
      }

      "can be reminded" in {
        tested.remind().reminded shouldBe true
      }
    }
  }
}
