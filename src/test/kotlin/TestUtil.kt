import com.revature.models.Employee
import com.revature.models.Reimbursement
import com.revature.models.enums.EmployeeType
import com.revature.models.enums.SpendingCategory
import org.mindrot.jbcrypt.BCrypt

class TestUtil {
    companion object {
        @JvmField val employee: Employee = Employee("user1", BCrypt.hashpw("pass1",BCrypt.gensalt()), EmployeeType.EMPLOYEE, "John", "Doe")
        @JvmField val fm: Employee = Employee("user2",BCrypt.hashpw("pass2",BCrypt.gensalt()), EmployeeType.FINANCE_MANAGER,"Jane","Doe")
        @JvmField val approvedReimb =
                Reimbursement(1, employee,"fakeurl.com/img", fm, 10.0, "description1", SpendingCategory.FOOD, 1)
        @JvmField val pendingReimb =
                Reimbursement(2, employee,"fakeurl.com/img2",amount=20.0,description="description2",category=SpendingCategory.TRAVEL, status=0)
        @JvmField val deniedReimb =
                Reimbursement(3, fm,"fakeurl.com/img3", fm, 100.00, "description3", SpendingCategory.LODGINGS, -1)
    }
}