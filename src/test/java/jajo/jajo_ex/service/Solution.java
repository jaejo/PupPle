package jajo.jajo_ex.service;

import java.util.*;

public class Solution {
    static int n = 6;
    static int[][] paths = new int[][]{{1, 2, 3}, {2, 3, 5}, {2, 4, 2}, {2, 5, 4}, {3, 4, 4}, {4, 5, 3}, {4, 6, 1}, {5, 6, 1}};
    static int[] gates = new int[]{1, 3};
    static int[] summits = new int[]{5};

    static public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        int[] answer = new int[2];

        ArrayList<ArrayList<int[]>> map = new ArrayList<>();

        for(int i=0; i<=n; i++) {
            map.add(new ArrayList<>());
        }

        for(int[] path: paths) {
            int a = path[0];
            int b = path[1];
            int len = path[2];

            map.get(a).add(new int[]{b, len});
            map.get(b).add(new int[]{a, len});
        }

        //출입구
        boolean[] isGate = new boolean[n+1];
        //정상
        boolean[] isSummit = new boolean[n+1];

        Queue<int[]> q = new LinkedList<>();
        int intensity = Integer.MAX_VALUE;

        for(int gate: gates) {
            isGate[gate] = true;
            for(int summit: summits){
                isSummit[summit] = true;
                //시작, 강도, 출입구, 정상, 정상 등반 여부
                q.offer(new int[]{gate, 0, gate, summit, 0});
            }

        }
        // while(!q.isEmpty()) {
        //     System.out.println(Arrays.toString(q.poll()));
        // }

        while(!q.isEmpty()) {
            int[] node = q.poll();
            int nodeA = node[0];
            int intensityA = node[1];
            int gate = node[2];
            int summit = node[3];
            int checkClimb = node[4];

            for(int[] temp: map.get(nodeA)) {
                int nodeB = temp[0];
                int intensityB = temp[1];
                // intensity 최대값 갱신
                intensityB = Math.max(intensityA, intensityB);
                // 정상 등반했을 경우
                if (checkClimb == 1) {
                    // 정상에 다시 왔을 경우
                    if (isSummit[nodeB]) continue;
                    // 출입구에 도착했을 경우
                    if (isGate[nodeB]) {
                        if (nodeB == gate) {
                            intensity = Math.min(intensity, intensityB);
                        }
                        continue;
                    }
                    // 정상을 등반하지 않았을 경우
                } else {
                    // 출입구에 도착했을 경우
                    if (isGate[nodeB]) continue;
                    // 정상에 도착했을 경우
                    if (isSummit[nodeB]) {
                        if (nodeB == summit) {
                            q.offer(new int[]{nodeB, intensityB, gate, summit, 1});
                        }
                        continue;
                    }
                }
                q.offer(new int[]{nodeB, intensityB, gate, summit, checkClimb});
            }
        }

        return answer;
    }

    public static void main(String[] args) {
        solution(n, paths, gates, summits);
    }
}


