package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.UserContactsRepository;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import org.springframework.stereotype.Repository;

@Repository
public class UserContactsRepositoryImpl extends GenericRepositoryImpl<Long, UserContacts> implements UserContactsRepository {
}
