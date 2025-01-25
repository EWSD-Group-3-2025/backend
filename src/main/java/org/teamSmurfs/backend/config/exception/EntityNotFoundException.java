/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:02 PM
 */
package org.teamSmurfs.backend.config.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}