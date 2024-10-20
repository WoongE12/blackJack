package com.the.ex;

import java.util.Scanner;

public class BlackJackGame {

    // ANSI 색상 코드
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    // 점수 계산 함수
    public static int calculateSum(int[] playerDeck, int playerDeckIndex) {
        int sum = 0;
        int aceCount = 0;

        for (int i = 0; i < playerDeckIndex; i++) {
            int cardValue = playerDeck[i] % 13;
            if (cardValue == 0) { // A 카드
                sum += 11;
                aceCount++;
            } else if (cardValue >= 10) { // J, Q, K 카드
                sum += 10;
            } else { // 2~10 카드
                sum += cardValue + 1;
            }
        }

        // A의 처리: 21을 넘으면 10씩 빼서 조정
        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }

        return sum;
    }

    // 카드 섞기 함수
    public static void shuffleDeck(int[] deck) {
        for (int i = 0; i < 10000; i++) {
            int cardIndex = (int) (Math.random() * 52);
            int tempCard = deck[0];
            deck[0] = deck[cardIndex];
            deck[cardIndex] = tempCard;
        }
    }

    // 카드 출력 함수
    public static void printCards(String playerName, int[] playerDeck, int playerDeckIndex, String[] cardShape, String[] cardNum) {
        System.out.print(playerName + "의 카드: ");
        for (int i = 0; i < playerDeckIndex; i++) {
            System.out.print("[" + cardShape[playerDeck[i] / 13] + " " + cardNum[playerDeck[i] % 13] + "] ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // 카드 만들기 (0~51까지 deck 배열에 저장)
        int[] deck = new int[52];
        for (int i = 0; i < deck.length; i++) {
            deck[i] = i;
        }

        // 카드 정보 배열
        String[] cardNum = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] cardShape = {"하트", "스페이드", "다이아", "클로버"};

        // 카드 랜덤하게 섞기
        shuffleDeck(deck);

        // 플레이어와 딜러 카드 초기화
        int[] playerDeck = new int[11]; // 최대 11장까지 받을 수 있음
        int playerDeckIndex = 0;
        int[] dealerDeck = new int[11];
        int dealerDeckIndex = 0;

        Scanner scanner = new Scanner(System.in);
        int deckIndex = 0; // 카드 인덱스

        // 플레이어와 딜러가 두 장의 카드를 받습니다.
        playerDeck[playerDeckIndex++] = deck[deckIndex++];
        playerDeck[playerDeckIndex++] = deck[deckIndex++];
        dealerDeck[dealerDeckIndex++] = deck[deckIndex++];
        dealerDeck[dealerDeckIndex++] = deck[deckIndex++];

        boolean isPlayerPlayFlag = true;
        int playerSum = calculateSum(playerDeck, playerDeckIndex);
        int dealerSum = calculateSum(dealerDeck, dealerDeckIndex);

        // 카드 정보 출력
        System.out.println("=======================================");
        printCards("플레이어", playerDeck, playerDeckIndex, cardShape, cardNum);
        System.out.println("딜러의 카드:");
        System.out.println("[" + cardShape[dealerDeck[0] / 13] + " " + cardNum[dealerDeck[0] % 13] + "] (보이지 않는 카드 포함)");
        System.out.println("플레이어 총점: " + playerSum);
        System.out.println("=======================================");

        // 게임 루프
        while (isPlayerPlayFlag) {
            // 플레이어 카드 받기
            System.out.print("카드를 받으시겠습니까? 1. 예 2. 아니오: ");
            String input = scanner.nextLine();
            if (input.equals("1") && playerDeckIndex < 11) {
                playerDeck[playerDeckIndex++] = deck[deckIndex++];
                playerSum = calculateSum(playerDeck, playerDeckIndex);
            } else {
                isPlayerPlayFlag = false;
            }

            // 카드 정보 출력
            System.out.println("=======================================");
            printCards("플레이어", playerDeck, playerDeckIndex, cardShape, cardNum);
            System.out.println("플레이어 총점: " + playerSum);
            System.out.println("=======================================");

            // 플레이어가 21을 넘으면 게임 종료
            if (playerSum > 21) {
                break;
            }
        }

        // 딜러의 카드와 점수 공개
        System.out.println("딜러의 카드:");
        System.out.println("=======================================");
        printCards("딜러", dealerDeck, dealerDeckIndex, cardShape, cardNum);
        dealerSum = calculateSum(dealerDeck, dealerDeckIndex);
        System.out.println("딜러 총점: " + dealerSum);
        System.out.println("=======================================");

        // 딜러의 플레이
        while (dealerSum < 17) {
            dealerDeck[dealerDeckIndex++] = deck[deckIndex++];
            dealerSum = calculateSum(dealerDeck, dealerDeckIndex);
        }

        // 승패 판정
        String winner;
        if (playerSum > 21) {
            winner = "딜러 승리";
        } else if (dealerSum > 21 || playerSum > dealerSum) {
            winner = "플레이어 승리";
        } else if (playerSum == dealerSum) {
            winner = "무승부";
        } else {
            winner = "딜러 승리";
        }

        // 승리자 강조 출력
        System.out.println("=======================================");
        if (winner.equals("플레이어 승리")) {
            System.out.println(ANSI_GREEN + "최종 결과: " + winner + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "최종 결과: " + winner + ANSI_RESET);
        }
        
        System.out.println("게임 종료");
        System.out.println("=======================================");
    }
}
