import com.yandex.taskmanager.service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

 class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}
