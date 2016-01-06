package xc0ffee.todo;

public class TodoItem {

    private final String mTodoText;

    public enum Status {
        STATUS_TODO,
        STATUS_DONE
    }
    private Status mStatus;

    public static class Builder {
        private final String text;
        private Status status;

        public Builder(String text) {
            this.text = text;
            status = Status.STATUS_TODO;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public TodoItem build() {
            return new TodoItem(text, status);
        }
    }

    private TodoItem(String text, Status status) {
        mTodoText = text;
        mStatus = status;
    }

    public String getTodoText() {
        return mTodoText;
    }

    public Status getStatus() {
        return mStatus;
    }
}
