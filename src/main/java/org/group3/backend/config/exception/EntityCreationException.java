/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2024
 * @Time : 09:02 PM
 */
package org.group3.backend.config.exception;

public class EntityCreationException extends RuntimeException {
    public EntityCreationException(String message) {
        super(message);
    }
}