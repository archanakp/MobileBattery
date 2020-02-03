package android.app.mobilebattery.Pojo;

public class OverTimeLsitPojo {

    private String id, agent_id, job_id, action, agent_name, job_issue, created_at, updated_at;

    private String job_receipt_no;

    public OverTimeLsitPojo(){}

    public String getJob_receipt_no() {
        return job_receipt_no;
    }

    public void setJob_receipt_no(String job_receipt_no) {
        this.job_receipt_no = job_receipt_no;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }


    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getJob_issue() {
        return job_issue;
    }

    public void setJob_issue(String job_issue) {
        this.job_issue = job_issue;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
