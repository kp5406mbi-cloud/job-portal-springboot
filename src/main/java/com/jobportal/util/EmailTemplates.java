package com.jobportal.util;

public class EmailTemplates {

    private EmailTemplates() {
    }

    private static final String BASE_URL =
            "https://job-portal-springboot-1.onrender.com";


    // =========================================================
    // COMMON EMAIL LAYOUT
    // =========================================================

    private static String layout(
            String title,
            String message,
            String detailsHtml,
            String buttonText,
            String buttonLink,
            String headerColor
    ) {

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Job Portal</title>
                </head>

                <body style="
                    margin:0;
                    padding:30px;
                    background:#f5f7fa;
                    font-family:Arial,Segoe UI,sans-serif;
                ">

                <table width="100%%"
                       cellpadding="0"
                       cellspacing="0">

                    <tr>
                        <td align="center">

                            <table width="650"
                                   cellpadding="0"
                                   cellspacing="0"
                                   style="
                                       background:#ffffff;
                                       border:1px solid #e5e7eb;
                                       border-radius:12px;
                                       overflow:hidden;
                                   ">

                                <!-- HEADER -->

                                <tr>
                                    <td style="
                                        background:%s;
                                        color:#ffffff;
                                        padding:25px;
                                        text-align:center;
                                        font-size:28px;
                                        font-weight:bold;
                                    ">

                                        Job Portal

                                    </td>
                                </tr>


                                <!-- CONTENT -->

                                <tr>
                                    <td style="
                                        padding:35px;
                                    ">

                                        <h2 style="
                                            margin-top:0;
                                            color:#111827;
                                        ">

                                            %s

                                        </h2>


                                        <p style="
                                            font-size:16px;
                                            color:#374151;
                                            line-height:1.8;
                                        ">

                                            %s

                                        </p>


                                        <!-- DETAILS -->

                                        <div style="
                                            background:#f8fafc;
                                            border:1px solid #e5e7eb;
                                            border-radius:8px;
                                            padding:20px;
                                            margin:25px 0;
                                            color:#374151;
                                            line-height:1.8;
                                        ">

                                            %s

                                        </div>


                                        <!-- BUTTON -->

                                        <p style="
                                            text-align:center;
                                            margin:30px 0;
                                        ">

                                            <a href="%s"
                                               style="
                                                   background:#2563eb;
                                                   color:#ffffff;
                                                   padding:12px 24px;
                                                   border-radius:8px;
                                                   text-decoration:none;
                                                   display:inline-block;
                                                   font-weight:bold;
                                               ">

                                                %s

                                            </a>

                                        </p>


                                        <!-- FOOTER -->

                                        <hr style="
                                            border:none;
                                            border-top:1px solid #e5e7eb;
                                            margin:30px 0;
                                        ">


                                        <p style="
                                            font-size:13px;
                                            color:#6b7280;
                                            text-align:center;
                                            line-height:1.6;
                                        ">

                                            This is an automated email from
                                            <b>Job Portal</b>.

                                            <br>

                                            Please do not reply directly
                                            to this email.

                                        </p>

                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>

                </table>

                </body>
                </html>
                """.formatted(
                headerColor,
                title,
                message,
                detailsHtml,
                buttonLink,
                buttonText
        );
    }


    // =========================================================
    // RECRUITER NOTIFICATION
    // =========================================================

    public static String recruiterNotification(
            String jobTitle,
            String company,
            String applicantEmail,
            String applicationDate,
            String applicationStatus,
            int atsScore,
            String missingSkills,
            String resumeFile
    ) {

        String details =
                "<b>Job Title:</b> " + jobTitle +
                        "<br>" +

                        "<b>Company:</b> " + company +
                        "<br>" +

                        "<b>Applicant Email:</b> " + applicantEmail +
                        "<br>" +

                        "<b>Application Date:</b> " + applicationDate +
                        "<br>" +

                        "<b>Application Status:</b> " +
                        applicationStatus +
                        "<br>" +

                        "<b>ATS Score:</b> " +
                        atsScore +
                        "%" +
                        "<br>" +

                        "<b>Missing Skills:</b> " +
                        (
                                missingSkills == null ||
                                        missingSkills.isBlank()
                                        ? "None"
                                        : missingSkills
                        ) +
                        "<br>" +

                        "<b>Resume File:</b> " +
                        resumeFile;


        return layout(
                "New Candidate Applied",

                "A new candidate has applied for one of your job postings.",

                details,

                "Open Recruiter Portal",

                BASE_URL + "/recruiter/dashboard",

                "#2563EB"
        );
    }

    public static String applicationSubmitted(
            String jobTitle,
            String company
    ) {

        String details =
                "<b>Job Title:</b> " + jobTitle +
                        "<br>" +
                        "<b>Company:</b> " + company +
                        "<br>" +
                        "<b>Application Status:</b> PENDING";

        return layout(
                "Application Submitted Successfully",

                "Your application has been submitted successfully.",

                details,

                "View My Applications",

                BASE_URL + "/user/applications",

                "#2563EB"
        );
    }

}