package com.example.chatgptproject.repository;

import com.example.chatgptproject.model.UserSessionDetails;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionDetailsRepository extends CrudRepository<UserSessionDetails,Long> {
}
