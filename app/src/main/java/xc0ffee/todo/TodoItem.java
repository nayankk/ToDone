package xc0ffee.todo;

public class TodoItem {

    private final String mTodoText;

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
        private String dueDate = new String();
        private String description = new String();
        private Priority priority = Priority.PRIOROTY_MEDIUM;

        public Builder(String text) {
            this.text = text;
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
            return new TodoItem(text, description, dueDate, priority);
        }
    }

    private TodoItem(String text, String description, String dueDate, Priority priority) {
        mTodoText = text;
        mDescription = description;
        mDueDate = dueDate;
        mPriority = priority;
    }

    public String getTodoText() {
        return mTodoText;
    }

    public String getTodoDesc() {
        return mDescription;
    }

    public String getDueDate() {
        return mDueDate;
    }

    public Priority getPriority() {
        return mPriority;
    }

}
