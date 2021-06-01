package com.gmail.yauheniylebedzeu.service.util;

import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.ArticleContent;
import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.ItemDescription;
import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.repository.model.OrderDetail;
import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.exception.ArticleContentNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.CartDetailNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.CommentNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.ItemDescriptionNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.ItemNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.OrderDetailNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.UserContactsNotReceivedException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getArticleContent;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getCart;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getComments;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getItem;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getItemDescription;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getOrderDetails;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getRole;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUser;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUserContacts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EntitiesServiceUtilTest {

    @Test
    void shouldGetRoleFromUserAndReceiveNotNullObject() {
        User user = new User();
        user.setRole(new Role());
        Role role = getRole(user);
        assertNotNull(role);
    }

    @Test
    void shouldGetRoleFromUserAndReceiveNullObject() {
        User user = new User();
        assertThrows(RoleNotReceivedException.class, () -> getRole(user));
    }

    @Test
    void shouldGetUserFromReviewAndReceiveNotNullObject() {
        Review review = new Review();
        review.setUser(new User());
        User user = getUser(review);
        assertNotNull(user);
    }

    @Test
    void shouldGetUserFromReviewAndReceiveNullObject() {
        Review review = new Review();
        assertThrows(UserNotReceivedException.class, () -> getUser(review));
    }

    @Test
    void shouldGetUserFromArticleAndReceiveNotNullObject() {
        Article article = new Article();
        article.setUser(new User());
        User user = getUser(article);
        assertNotNull(user);
    }

    @Test
    void shouldGetUserFromArticleAndReceiveNullObject() {
        Article article = new Article();
        assertThrows(UserNotReceivedException.class, () -> getUser(article));
    }

    @Test
    void shouldGetUserFromCommentAndReceiveNotNullObject() {
        Comment comment = new Comment();
        comment.setUser(new User());
        User user = getUser(comment);
        assertNotNull(user);
    }

    @Test
    void shouldGetUserFromCommentAndReceiveNullObject() {
        Comment comment = new Comment();
        assertThrows(UserNotReceivedException.class, () -> getUser(comment));
    }

    @Test
    void shouldGetUserFromOrderAndReceiveNotNullObject() {
        Order order = new Order();
        order.setUser(new User());
        User user = getUser(order);
        assertNotNull(user);
    }

    @Test
    void shouldGetUserFromOrderAndReceiveNullObject() {
        Order order = new Order();
        assertThrows(UserNotReceivedException.class, () -> getUser(order));
    }

    @Test
    void shouldGetUserContactsAndReceiveNotNullObject() {
        User user = new User();
        user.setContacts(new UserContacts());
        UserContacts userContacts = getUserContacts(user);
        assertNotNull(userContacts);
    }

    @Test
    void shouldGetUserContactsAndReceiveNullObject() {
        User user = new User();
        assertThrows(UserContactsNotReceivedException.class, () -> getUserContacts(user));
    }

    @Test
    void shouldGetItemFromOrderDetailAndReceiveNotNullObject() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setItem(new Item());
        Item item = getItem(orderDetail);
        assertNotNull(item);
    }

    @Test
    void shouldGetItemFromOrderDetailAndReceiveNullObject() {
        OrderDetail orderDetail = new OrderDetail();
        assertThrows(ItemNotReceivedException.class, () -> getItem(orderDetail));
    }

    @Test
    void shouldGetItemFromCartDetailAndReceiveNotNullObject() {
        CartDetail cartDetail = new CartDetail();
        cartDetail.setItem(new Item());
        Item item = getItem(cartDetail);
        assertNotNull(item);
    }

    @Test
    void shouldGetItemFromCartDetailAndReceiveNullObject() {
        CartDetail cartDetail = new CartDetail();
        assertThrows(ItemNotReceivedException.class, () -> getItem(cartDetail));
    }

    @Test
    void shouldGetItemDescriptionFromItemDetailAndReceiveNotNullObject() {
        Item item = new Item();
        item.setItemDescription(new ItemDescription());
        ItemDescription itemDescription = getItemDescription(item);
        assertNotNull(itemDescription);
    }

    @Test
    void shouldGetItemDescriptionFromItemDetailAndReceiveNullObject() {
        Item item = new Item();
        assertThrows(ItemDescriptionNotReceivedException.class, () -> getItemDescription(item));
    }

    @Test
    void shouldGetCartFromUserWithoutNullObjectsInside() {
        User user = new User();
        Set<CartDetail> cart = new HashSet<>();
        cart.add(new CartDetail());
        user.setCart(cart);
        Set<CartDetail> actualCart = getCart(user);
        assertEquals(cart, actualCart);
    }

    @Test
    void shouldGetCartFromUserWithNullObjectInside() {
        User user = new User();
        Set<CartDetail> cart = new HashSet<>();
        cart.add(null);
        user.setCart(cart);
        assertThrows(CartDetailNotReceivedException.class, () -> getCart(user));
    }

    @Test
    void shouldGetOrderDetailsFromOrderWithoutNullObjectsInside() {
        Order order = new Order();
        Set<OrderDetail> orderDetails = new HashSet<>();
        orderDetails.add(new OrderDetail());
        order.setOrderDetails(orderDetails);
        Set<OrderDetail> actualOrderDetails = getOrderDetails(order);
        assertEquals(orderDetails, actualOrderDetails);
    }

    @Test
    void shouldGetOrderDetailsFromOrderWithNullObjectInside() {
        Order order = new Order();
        Set<OrderDetail> orderDetails = new HashSet<>();
        orderDetails.add(null);
        order.setOrderDetails(orderDetails);
        assertThrows(OrderDetailNotReceivedException.class, () -> getOrderDetails(order));
    }

    @Test
    void shouldGetArticleContentAndReceiveNotNullObject() {
        Article article = new Article();
        article.setContent(new ArticleContent());
        ArticleContent articleContent = getArticleContent(article);
        assertNotNull(articleContent);
    }

    @Test
    void shouldGetArticleContentAndReceiveNullObject() {
        Article article = new Article();
        assertThrows(ArticleContentNotReceivedException.class, () -> getArticleContent(article));
    }

    @Test
    void shouldGetCommentsFromArticleWithoutNullObjectsInside() {
        Article article = new Article();
        Set<Comment> comments = new HashSet<>();
        comments.add(new Comment());
        article.setComments(comments);
        Set<Comment> actualComments = getComments(article);
        assertEquals(comments, actualComments);
    }

    @Test
    void shouldGetCommentsFromArticleWithNullObjectInside() {
        Article article = new Article();
        Set<Comment> comments = new HashSet<>();
        comments.add(null);
        article.setComments(comments);
        assertThrows(CommentNotReceivedException.class, () -> getComments(article));
    }
}