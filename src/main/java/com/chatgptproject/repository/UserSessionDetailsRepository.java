package com.chatgptproject.repository;

import com.chatgptproject.model.UserSessionDetails;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionDetailsRepository extends CrudRepository<UserSessionDetails,Long> {
}
