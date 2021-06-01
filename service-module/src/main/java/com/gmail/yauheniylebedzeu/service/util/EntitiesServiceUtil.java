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

import java.util.Objects;
import java.util.Set;

public class EntitiesServiceUtil {

    public static Role getRole(User user) {
        Role role = user.getRole();
        if (Objects.isNull(role)) {
            throw new RoleNotReceivedException(String.format("Couldn't receive the role of the user with id = %d",
                    user.getId()));
        }
        return role;
    }

    public static User getUser(Review review) {
        User user = review.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotReceivedException(String.format("Couldn't receive the user for the review with id = %d",
                    review.getId()));
        }
        return user;
    }

    public static User getUser(Article article) {
        User user = article.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotReceivedException(String.format("Couldn't receive the user for the article with id = %d",
                    article.getId()));
        }
        return user;
    }

    public static User getUser(Comment comment) {
        User user = comment.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotReceivedException(String.format("Couldn't receive the user for the comment with id = %d",
                    comment.getId()));
        }
        return user;
    }

    public static User getUser(Order order) {
        User user = order.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotReceivedException(String.format("Couldn't receive the user for the order with id = %d",
                    order.getId()));
        }
        return user;
    }


    public static UserContacts getUserContacts(User user) {
        UserContacts contacts = user.getContacts();
        if (Objects.isNull(contacts)) {
            throw new UserContactsNotReceivedException(String.format("Couldn't receive the contacts of the user with" +
                    " id = %d", user.getId()));
        }
        return contacts;
    }

    public static Item getItem(OrderDetail orderDetail) {
        Item item = orderDetail.getItem();
        if (Objects.isNull(item)) {
            throw new ItemNotReceivedException(String.format("Couldn't receive the item for the order detail with id" +
                    " = %d", orderDetail.getId()));
        }
        return item;
    }

    public static Item getItem(CartDetail cartDetail) {
        Item item = cartDetail.getItem();
        if (Objects.isNull(item)) {
            throw new ItemNotReceivedException(String.format("Couldn't receive the item for the cart detail with id = %d",
                    cartDetail.getId()));
        }
        return item;
    }

    public static ItemDescription getItemDescription(Item item) {
        ItemDescription itemDescription = item.getItemDescription();
        if (Objects.isNull(itemDescription)) {
            throw new ItemDescriptionNotReceivedException(String.format("Couldn't receive the description of the item with" +
                    " id = %d", item.getId()));
        }
        return itemDescription;
    }

    public static Set<CartDetail> getCart(User user) {
        Set<CartDetail> cart = user.getCart();
        for (CartDetail cartDetail : cart) {
            if (Objects.isNull(cartDetail)) {
                throw new CartDetailNotReceivedException(String.format("Couldn't receive the cart detail of the user with id %d",
                        user.getId()));
            }
        }
        return cart;
    }

    public static Set<OrderDetail> getOrderDetails(Order order) {
        Set<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            if (Objects.isNull(orderDetail)) {
                throw new OrderDetailNotReceivedException(String.format("Couldn't receive the detail of the order with" +
                        " id = %d", order.getId()));
            }
        }
        return orderDetails;
    }

    public static ArticleContent getArticleContent(Article article) {
        ArticleContent content = article.getContent();
        if (Objects.isNull(content)) {
            throw new ArticleContentNotReceivedException(String.format("Couldn't receive the content for the article" +
                    " with id = %d", article.getId()));
        }
        return content;
    }

    public static Set<Comment> getComments(Article article) {
        Set<Comment> comments = article.getComments();
        for (Comment comment : comments) {
            if (Objects.isNull(comment)) {
                throw new CommentNotReceivedException(String.format("Couldn't receive the comment for the article with" +
                        " id = %d", article.getId()));
            }
        }
        return comments;
    }
}