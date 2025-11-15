package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * Generates a plaintext statement from an invoice.
 */

public class StatementPrinter {

    private final Invoice invoice;
    private final Map<String, Play> plays;

    /**
     * Constructs a StatementPrinter.
     *
     * @param invoice the invoice to print
     * @param plays   a map of playID to Play objects
     */
    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns the formatted statement for this invoice.
     *
     * @return the formatted statement
     */
    public String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;

        final StringBuilder result =
                new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());

        final NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

        for (Performance perf : invoice.getPerformances()) {

            final int thisAmount = calculateAmount(perf);

            // volume credits
            volumeCredits += Math.max(
                    perf.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD,
                    0
            );

            if ("comedy".equals(getPlay(perf).getType())) {
                volumeCredits += perf.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
            }

            final String formattedAmount =
                    formatter.format(thisAmount / Constants.PERCENT_FACTOR);

            // print line for this performance
            result.append(
                    String.format(
                            "  %s: %s (%s seats)%n",
                            getPlay(perf).getName(),
                            formattedAmount,
                            perf.getAudience()
                    )
            );

            totalAmount += thisAmount;
        }

        result.append(
                String.format(
                        "Amount owed is %s%n",
                        formatter.format(totalAmount / Constants.PERCENT_FACTOR)
                )
        );
        result.append(
                String.format("You earned %s credits%n", volumeCredits)
        );

        return result.toString();
    }

    private Play getPlay(Performance perf) {
        return plays.get(perf.getPlayID());
    }

    /**
     * Calculates the charge for a single performance.
     *
     * @param perf the performance instance
     * @return the cost in cents
     * @throws IllegalArgumentException if the play type is unknown
     */
    private int calculateAmount(Performance perf) {
        final int audience = perf.getAudience();
        final String type = getPlay(perf).getType();

        int result;

        switch (type) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (audience > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (audience - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;

            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (audience > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (audience - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * audience;
                break;

            default:
                throw new IllegalArgumentException("unknown type: " + type);
        }

        return result;
    }

}
