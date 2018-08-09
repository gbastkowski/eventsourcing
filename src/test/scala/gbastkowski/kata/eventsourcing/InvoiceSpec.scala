package gbastkowski.kata.eventsourcing

import java.time.LocalDateTime
import org.scalatest._

class InvoiceSpec extends FreeSpec with Matchers {
  "An invoice" - {
    val coke = InvoiceItem("Coke", 1)
    val fries = InvoiceItem("Fries", 2)

    "which is new and empty" - {
      val invoice = new Invoice()

      "can add an item" in {
        invoice.addItem(coke).items should have size 1
      }

      "can remove an item" in {
        invoice.addItem(coke).removeItem(coke).items shouldBe empty
      }

      "can change it's recipient" in {
        invoice.changeRecipient(Some("Recipient")).recipient shouldBe Some("Recipient")
      }

      "can be sent" in {
        invoice.send().sent shouldBe defined
      }

      "cannot be reminded" in {
        an[IllegalArgumentException] should be thrownBy invoice.remind()
      }

      "cannot receive payments" in {
        an[IllegalArgumentException] should be thrownBy invoice.paymentReceived(LocalDateTime.now())
      }
    }

    "with two items" - {
      val invoice = new Invoice().addItem(coke).addItem(fries)
      "has a correct totalAmount" in {
        invoice.totalAmount shouldBe 3
      }
    }

    "which has been sent" - {
      val invoice = new Invoice().addItem(coke).send()

      "cannot add items" in {
        an[IllegalArgumentException] should be thrownBy invoice.addItem(coke)
      }

      "cannot remove items" in {
        an[IllegalArgumentException] should be thrownBy invoice.removeItem(invoice.items(0))
      }

      "can be reminded" in {
        invoice.remind().reminded shouldBe defined
      }
    }
  }
}
