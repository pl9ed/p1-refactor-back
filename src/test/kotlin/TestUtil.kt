import com.revature.models.Employee
import com.revature.models.enums.EmployeeType
import org.mindrot.jbcrypt.BCrypt

class TestUtil {
    companion object {
        val employee: Employee = Employee("user1", BCrypt.hashpw("pass1",BCrypt.gensalt()), EmployeeType.EMPLOYEE, "John", "Doe")
        val fm: Employee = Employee("user2",BCrypt.hashpw("pass2",BCrypt.gensalt()), EmployeeType.FINANCE_MANAGER,"Jane","Doe")
    }
}