package com.example.desklabv3.core.util

enum class ActionType {
    Home {
        override fun toString() = "0"
        override fun tag() = ""
    },
    LeaveDetail {
        override fun toString() = "1"
        override fun tag() = ""
    },
    LeaveDetailApproval {
        override fun toString() = "1x"
        override fun tag() = ""
    },
    WfhDetailApproval {
        override fun toString() = "2x"
        override fun tag() = ""
    },
    WfhDetail {
        override fun toString() = "2"
        override fun tag() = ""
    },
    EPMActivity {
        override fun toString() = "3"
        override fun tag() = ""
    },
    EPMActivityApproval {
        override fun toString() = "3x"
        override fun tag() = ""
    },
    SpecialWorkDetail {
        override fun toString(): String = "4"
        override fun tag() = ""
    },
    SpecialWorkDetailApproval {
        override fun toString(): String = "4x"
        override fun tag() = ""
    },
    Activities {
        override fun toString() = "ACTIVITIES"
        override fun tag() = "AKTIVITAS"
    },
    Leave {
        override fun toString() = "LEAVE"
        override fun tag() = "CUTI"
    },
    SpecialWork {
        override fun toString() = "SPECIAL_WORK"
        override fun tag() = "KERJA KHUSUS"
    },
    Delegate {
        override fun toString() = "DELEGATE"
        override fun tag() = "DELEGASI"
    },
    GeneralEvent {
        override fun toString() = "GENERAL_EVENT"
        override fun tag() = "GENERAL EVENT"
    },
    PeopleDevelopment {
        override fun toString() = "PEOPLE_DEVELOPMENT"
        override fun tag() = "PEOPLE DEVELOPMENT"
    },
    EmployeeBenefit {
        override fun toString() = "EMPLOYEE_BENEFIT"
        override fun tag() = "EMPLOYEE BENEFIT"
    };

    abstract fun tag(): String
}