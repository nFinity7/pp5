package juras.sales.offerting;

import juras.sales.cart.Cart;
import juras.sales.cart.CartItem;
import juras.sales.Product;
import juras.sales.ProductDetailsProvider;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OfferMaker {
    private ProductDetailsProvider productDetailsProvider;

    public OfferMaker(ProductDetailsProvider productDetailsProvider) {
        this.productDetailsProvider = productDetailsProvider;
    }

    public Offer createAnOffer(Cart cart) {
        //prod_id, name, quantity, price
        List<OfferLine> lines = cart.getItems()
                .stream()
                .map(this::createOfferLine)
                .collect(Collectors.toList());

        BigDecimal offerTotal = lines.stream()
                .map(line -> line.getTotal())
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return new Offer(offerTotal, lines);
    }

    private OfferLine createOfferLine(CartItem cartItem) {
        Product details = productDetailsProvider.getDetails(cartItem.getProductId());
        return new OfferLine(
                cartItem.getProductId(),
                details.getName(),
                cartItem.getQuantity(),
                details.getPrice()
        );
    }
}
