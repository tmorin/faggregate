package todo.model.view;

import java.util.List;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * The view presents a list of {@link TodoListView}.
 */
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
