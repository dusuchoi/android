package kr.cds.mosquitoforecast;


public class Data {
    public static String grade[] = {
            "1단계 : 쾌적(하)",
            "1단계 : 쾌적(중)",
            "1단계 : 쾌적(상)",

            "2단계 : 관심(하)",
            "2단계 : 관심(중)",
            "2단계 : 관심(상)",

            "3단계 : 주의(하)",
            "3단계 : 주의(중)",
            "3단계 : 주의(상)",

            "4단계 : 불쾌(하)",
            "4단계 : 불쾌(중)",
            "4단계 : 불쾌(상)",
    };

    public static String defence_activity[] = {
            "없음",
            "없음",
            "* 모기 침입통로 보완\n" +
                    "* 창문과 문에 방충망 사용하기",

            "* 늦은 시간 현관문 열어놓지 않기\n" +
                    "* 가급적 냉방기 사용",
            "* 주택 내로 모기 침입 주의\n" +
                    "* 모기 침입통로 수리",
            "* 문과 창문 방충망 수리\n" +
                    "* 정화조의 금이나 틈새 수리하기",

            "* 단독주택 거주민 모기장 사용\n" +
                    "* 출입문과 창문을 열어 환기하는 것을 자제하고 냉방기 사용",
            "* 주간 풀숲 근접지역 숲모기의 흡혈공격 주의\n" +
                    "* 야외활동시 가급적 긴팔과 긴바지, 양말 착용\n" +
                    "* 아기침대와 유모차, 아기 케리어에 모기장 사용",
            "* 오후 7시 이후 출입문, 창문(방충망 없는) 열어 놓지 않도록 주의\n" +
                    "* 아파트 거주민 모기장 사용",

            "* 어린이 야간활동 자제\n" +
                    "*취침 시 반드시 모기장 사용",
            "* 야외 활동 자제\n" +
                    "* 숲 가까이 활동 시 숲모기의 흡혈공격 주의",
            "* 오후 6시 이후 출입문, 창문(방충망 없는) 열어 놓지 않도록 주의\n" +
                    "* 야외 활동 후 바로 샤워 실시"
    };
    public static String aggressive_activity[] = {
            "* 야외 모기서식지 형성이 완전하지 않으므로 별도의 모기 방제방법이 필요하지 않음",
            "* 생활주변 모기유충 서식지 관찰",
            "* 생활주변 모기유충 서식지 파악",

            "* 모기유충 서식 관찰",
            "* 생활주변(주택, 상가, 시장)의 방치된 모기 발생원을 제거하거나 물 비우기 (버려진 용기, 헌타이어, 깡통 등)",
            "* 제거 못한 인공용기 빗물 고임 차단\n" +
                    "* 주택 옥상에 받아 놓은 빗물 통에 뚜껑 설치\n" +
                    "* 단독주택 정화조 환기구에 모기망 설치",

            "* 관심단계의 모기발생원에 대한 관리 유지\n" +
                    "* 빗물 고인 곳 모기유충 서식차단",
            "* 주 1회 화분, 양동이, 반려동물 식기 등에 고여있는 물을 비워내고 문질러 닦아 뒤집어 놓기\n" +
                    "* 뚜껑이 없거나 물을 비워낼 수 없는 통은 모기가 통과할 수 없는 촘촘한 망으로 덮기\n" +
                    "* 단독주택의 구석진 담장 밑(그늘 진 곳)에 낮에 모기성충이 휴식하고 있으므로 가정용 에어로졸로 방제\n" +
                    "* 집주변에 화초나 풀이 무성하게 자란 곳, 마루 틈, 보일러 실, 창고, 지하계단에 휴식하는 모기가 발견되면 가정용 에어로졸로 방제",
            "* 대규모 야외 모기 서식지(도랑, 빗물펌프장 주변, 맨홀, 정화조, 빗물 고인 웅덩이 등) 모기유충 서식을 확인하고 보건소에 신고\n" +
                    "* 생활주변 모기유충 방제\n" +
                    "* 야간에 활동 시 어린이는 모기기피제(어린이 전용) 사용 (주의사항 반드시 확인)\n" +
                    "* 야외활동시 모기퇴치제 사용",

            "* 주의단계에서 제시한 모기성충 휴식처에 에어로졸을 분사하여 방제\n" +
                    "* 어른도 야간활동 시 기피제 사용\n" +
                    "* 출입문 주변이나 밖 벽면에 가정용 살충제 처리\n" +
                    "* 현관문 밖과 벽면에 가정용 에어로졸 분사하여 살충제를 잔류시킴",
            "* 생활주변 소형 모기 발생원 적극적 제거, 하룻밤 주택내 모기 5마리 이상 침입 시 관할보건소 모기조사 신고\n" +
                    "* 취침 2시간 전 전자모기향 사용, 취침 시 모기향 꺼짐을 확인하고 환기 후 취침",
            "* 생활주변 물이 고인 곳에 모기유충 서식을 확인하고 관할보건소 방역기동반에 적극적으로 신고\n" +
                    "* 주 1회씩 가까운 대형 모기 발생원을 확인하고 보건소에 신고하여 방제 요청"
    };

    public static String organization_activity[] = {
            "*월동모기 방제",
            "*모기유충 서식지 감시",
            "*모기유충 서식지 감시",

            "*야외 모기유충 서식지 제거\n" +
                    "*지역별로 물이 고일 수 있는 가능성을 제거",
            "*야외 모기유충 서식 감시 및 관리",
            "*야외 서식 모기유충 방제\n" +
                    "*모기 성충발생 모니터링",

            "*야외 모기유충 방제\n" +
                    "*모기 성충발생 모니터링",
            "*야외 모기유충 서식 감시 및 관리\n" +
                    "*버려진 세탁기, 냉장고, 변기와 같이 물이 쌓일 수 있는 폐기물 방치 관리",
            "*방제모기성충 발생밀도 감시\n" +
                    "*모기의 수와 분포에 대해 조사하고 유충 서식지 제거에 대한 영향력을 평가",

            "*모기유충 및 성충\n" +
                    "*민원 발생지역 적극 조사",
            "*모기유충 및 성충\n" +
                    "*민원 발생지역 적극 조사",
            "*모기유충 및 성충\n" +
                    "*민원 발생지역 적극 조사\n" +
                    "*장기적으로 상하수도 정비, 재활용수거함 정비, 재활용 타이어 적치방안 강구",
    };

}

