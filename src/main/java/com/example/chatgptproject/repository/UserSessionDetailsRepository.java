package com.example.chatgptproject.repository;

import com.example.chatgptproject.dto.UserSessionDetails;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionDetailsRepository extends CrudRepository<UserSessionDetails,Long> {
}
