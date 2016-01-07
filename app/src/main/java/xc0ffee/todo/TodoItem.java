package xc0ffee.todo;

public class TodoItem {

    private final String mTodoText;

    public enum Status {
        STATUS_TODO,
        STATUS_DONE
    }
    private final Status mStatus;

    public enum Priority {
        PRIOROTY_LOW,
        PRIOROTY_MEDIUM,
        PRIOROTY_HIGH
    }
    private final Priority mPriority;

    private final String mDueDate;

    private final String mDescription;

    public static class Builder {
        private final String text;
        private Status status;
        private String dueDate;
        private String description;
        private Priority priority;

        public Builder(String text) {
            this.text = text;
            status = Status.STATUS_TODO;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder dueDate(String dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public TodoItem build() {
            return new TodoItem(text, status, description, dueDate, priority);
        }
    }

    private TodoItem(String text, Status status, String description, String dueDate, Priority priority) {
        mTodoText = text;
        mStatus = status;
        mDescription = description;
        mDueDate = dueDate;
        mPriority = priority;
    }

    public String getTodoText() {
        return mTodoText;
    }

    public Status getStatus() {
        return mStatus;
    }
}
