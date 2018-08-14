package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    val coke = InvoiceItem(1, "Coke", 1)
    val fries = InvoiceItem(2, "Fries", 2)

    "which is new and empty" - {
      def invoice = new Invoice

      "can add an item" in {
        val tested = invoice
        tested.addItem(coke)
        tested.items should have size 1
      }

      "can change it's recipient" in {
        val tested = invoice
        tested.changeRecipient(Some("Recipient"))
        tested.recipient shouldBe Some("Recipient")
      }

      "can be sent" in {
        val tested = invoice
        tested.send()
        tested.sent shouldBe true
      }

      "cannot be reminded" in {
        an[IllegalArgumentException] should be thrownBy invoice.remind()
      }

      "cannot receive payments" in {
        an[IllegalArgumentException] should be thrownBy invoice.paymentReceived(LocalDateTime.now())
      }
    }

    "with two items" - {
      def invoice = {
        val i = new Invoice
        i.addItem(coke)
        i.addItem(fries)
        i
      }

      "can remove an item" in {
        val tested = invoice
        tested.removeItem(coke.id)
        tested.items should have size(1)
      }

      "has a correct totalAmount" in {
        invoice.totalAmount shouldBe 3
      }
    }

    "which has been sent" - {
      val tested = new Invoice
      tested.addItem(coke)
      tested.send()

      "cannot add items" in {
        an[IllegalArgumentException] should be thrownBy tested.addItem(coke)
      }

      "cannot remove items" in {
        an[IllegalArgumentException] should be thrownBy tested.removeItem(0)
      }

      "can be reminded" in {
        tested.remind()
        tested.reminded shouldBe true
      }
    }
  }
}
