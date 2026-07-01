package com.hdecoded.finman.service;

import com.hdecoded.finman.config.FinmanProperties;
import com.hdecoded.finman.dto.ExpenseDTO;
import com.hdecoded.finman.entity.ProfileEntity;
import com.hdecoded.finman.repository.ProfileRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;
    private final FinmanProperties finmanProperties;

    @Scheduled(cron = "0 0 20 * * *", zone = "Asia/Kolkata")
    public void sendDailyIncomeExpenseReminder() {

        log.info("Job Started: sendDailyIncomeExpenseReminder()");

        String frontendUrl = finmanProperties.getFrontend().getUrl();
        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                + "This is a friendly reminder to add your income and expenses for today.<br><br>"
                + "<a href=" + frontendUrl
                + " style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Money Manager</a>"
                + "<br><br>Best regards,<br>FinMan";
            emailService.sendEmail(profile.getEmail(),
                "Daily reminder: Add your income and expenses for today.", body);
        }

        log.info("Job Finished: sendDailyIncomeExpenseReminder()");
    }

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Kolkata")
    public void sendDailyExpenseSummary() {
        log.info("Job Started: sendDailyExpenseSummary()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            List<ExpenseDTO> todaysExpenses = expenseService.getExpensesForUserOnDate(
                profile.getId(), LocalDate.now(ZoneId.of("Asia/Kolkata")));
            log.info("Todays expenses: {}", todaysExpenses.toString());
            if (!todaysExpenses.isEmpty()) {
                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append(
                    "<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>S.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;'>Amount</th><th style='border:1px solid #ddd;padding:8px;'>Category</th></tr>");
                int i = 1;
                for (ExpenseDTO expense : todaysExpenses) {
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++)
                        .append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(expense.getName()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(expense.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A")
                        .append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hi " + profile.getFullName()
                    + ",<br/><br/> Here is a summary of your expenses for today:<br/><br/>" + table
                    + "<br/><br/>Best regards,<br/>FinMan";
                emailService.sendEmail(profile.getEmail(),
                    "Here is a summary of your expenses for today.", body);
            }
        }
    }
}
