package com.dawasakhi.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("error")
    private ErrorDetail error;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("path")
    private String path;

    public ErrorResponse() {
    }

    public ErrorResponse(Boolean success, ErrorDetail error, LocalDateTime timestamp, String path) {
        this.success = success;
        this.error = error;
        this.timestamp = timestamp;
        this.path = path;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ErrorDetail getError() {
        return error;
    }

    public void setError(ErrorDetail error) {
        this.error = error;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {
        @JsonProperty("code")
        private String code;

        @JsonProperty("message")
        private String message;

        @JsonProperty("details")
        private Object details;

        public ErrorDetail() {
        }

        public ErrorDetail(String code, String message, Object details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }

        public static ErrorDetailBuilder builder() {
            return new ErrorDetailBuilder();
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getDetails() {
            return details;
        }

        public void setDetails(Object details) {
            this.details = details;
        }

        public static class ErrorDetailBuilder {
            private String code;
            private String message;
            private Object details;

            public ErrorDetailBuilder code(String code) {
                this.code = code;
                return this;
            }

            public ErrorDetailBuilder message(String message) {
                this.message = message;
                return this;
            }

            public ErrorDetailBuilder details(Object details) {
                this.details = details;
                return this;
            }

            public ErrorDetail build() {
                return new ErrorDetail(code, message, details);
            }
        }
    }

    public static class ErrorResponseBuilder {
        private Boolean success;
        private ErrorDetail error;
        private LocalDateTime timestamp;
        private String path;

        public ErrorResponseBuilder success(Boolean success) {
            this.success = success;
            return this;
        }

        public ErrorResponseBuilder error(ErrorDetail error) {
            this.error = error;
            return this;
        }

        public ErrorResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ErrorResponseBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(success, error, timestamp, path);
        }
    }
}