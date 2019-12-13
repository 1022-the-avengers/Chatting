package top.arron206.whisper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.arron206.whisper.dao.FriendshipRepository;
import top.arron206.whisper.dao.UserRepository;
import top.arron206.whisper.dto.UserInformation;
import top.arron206.whisper.entity.Friendship;
import top.arron206.whisper.entity.User;
import top.arron206.whisper.service.RelationshipService;
import top.arron206.whisper.vo.RelationshipMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    FriendshipRepository friendshipRepository;
    @Autowired
    UserRepository userRepository;

    public boolean isFriend(User customer, User host){
        return friendshipRepository.findByCustomAndAndHost(host, customer) != null;
    }

    public void saveRelationShip(RelationshipMessage relationshipMessage) {
        User host = new User(relationshipMessage.getHostId());
        User custom = new User(relationshipMessage.getCustomId());
        Friendship friendshipInDB = this.friendshipRepository.findByCustomAndAndHost(custom, host);
        if (friendshipInDB != null)
            this.friendshipRepository.delete(friendshipInDB);
        Friendship friendship = new Friendship(relationshipMessage.getGroupName(), host, custom);
        this.friendshipRepository.save(friendship);
    }

    public void deleteRelationShip(int hostId, int customId) {
        User host = new User(hostId);
        User custom = new User(customId);
        Friendship friendshipInDB = this.friendshipRepository.findByCustomAndAndHost(custom, host);
        this.friendshipRepository.delete(friendshipInDB);
    }

    public Map<String, List<UserInformation>> getRelationShips(int hostId) {
        User host = this.userRepository.findById(hostId);
        List<Friendship> friendships = host.getHostFriendship();
        Map<String, List<UserInformation>> relations = new LinkedHashMap<>();
        for (Friendship friendship : friendships) {
            String groupName = friendship.getGroupName();
            if (!relations.containsKey(groupName))
                relations.put(groupName, new ArrayList<UserInformation>());
            User user = this.userRepository.findById(friendship.getCustom().getId());
            UserInformation userInformation = new UserInformation(user.getId(), user.getNickname(),
                    user.getPic(), user.getGender(), user.getAge());
            relations.get(groupName).add(userInformation);
        }
        return relations;
    }
}
