package com.codecool.shop.controller;

import com.codecool.shop.config.Initializer;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.CartItem;
import com.codecool.shop.util.CartStatusType;
import com.codecool.shop.util.implementation_factory.ImplementationFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    private static final Logger cartLogger = LoggerFactory.getLogger(PaymentController.class);
    private static final ImplementationFactory IMPLEMENTATION_FACTORY = Initializer.getImplementationFactory();

    private CartDao cartDataManager = IMPLEMENTATION_FACTORY.getCartDataManagerInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        cartDataManager.updateCartStatusBy(cartDataManager.getLargestCartId(), CartStatusType.UNFINISHED);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("cartDataManager", cartDataManager);
        context.setVariable("numberOfProductsInLastCart", cartDataManager.getLastCart().getCartItemList().size());
        engine.process("product/cart.html", context, resp.getWriter());

        cartLogger.info("Shopping cart editor page displayed");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<CartItem> currentCartsItemList = cartDataManager.getLastCart().getCartItemList();
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            for (CartItem cartItem : currentCartsItemList) {
                int newQuantity = Integer.parseInt(req.getParameter(String.valueOf(cartItem.id)));
                if (newQuantity < 1) {
                    currentCartsItemList.remove(cartItem);
                    cartLogger.info("{} is removed from shopping cart.", cartItem.getProduct().getName());
                    repeat = true;
                    break;

                } else if (newQuantity != cartItem.getQuantity()) {
                    cartItem.setQuantity(newQuantity);
                    cartLogger.info("New quantity for {} is set to {}", cartItem.getProduct().getName(), newQuantity);
                }
            }
        }

        // TODO: SAVE TOTAL PRICE IN DATABASE: USE METHOD IN REVIEW CONTROLLER
        cartDataManager.saveChangesInCartAutomatically(currentCartsItemList);

        if (currentCartsItemList.size() > 0) {
            resp.sendRedirect("/review");
        } else {
            resp.sendRedirect("/");
            cartLogger.info("All products were removed from shopping cart.");
        }

    }
}

