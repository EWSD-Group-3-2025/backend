/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 09:05 PM
 */
package org.teamSmurfs.backend.config.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}