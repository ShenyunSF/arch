package zookeeper.curator.discovery;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("what-ever-you-want")
public class InstanceInfo {
private String myAppName;
private String description;
}
