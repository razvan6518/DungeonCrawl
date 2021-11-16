public class Kata {
    public static int GetParticipants(int handshakes)
    {
        int participants = 1;
        for (int count = 0; count < handshakes; participants ++)
            count += participants;
        return participants;
    }

    public static void main(String[] args) {
        System.out.println(GetParticipants(100));
    }
}
