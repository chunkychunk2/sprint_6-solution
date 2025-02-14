import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.service.InMemoryTaskManager;
import com.yandex.taskmanager.service.Managers;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() throws IOException {
        super.setUp();
    }
}
