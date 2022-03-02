package todo.model.view;

import java.util.List;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class TodoListsView {

    /**
     * The lists.
     */
    @NonNull
    List<TodoListView> lists;
}
