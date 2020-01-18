package quaternary.zombievillagecontrol.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

@SuppressWarnings("unused")
public class ClassTransformer implements IClassTransformer, Opcodes {
	private static final String STRUCTURE_VILLAGE_PIECES_START = "net.minecraft.world.gen.structure.StructureVillagePieces$Start";
	private static final String ENTITY_ZOMBIE_VILLAGER = "net.minecraft.entity.monster.EntityZombieVillager";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(!transformedName.equals(STRUCTURE_VILLAGE_PIECES_START) && !transformedName.equals(ENTITY_ZOMBIE_VILLAGER)) {
			return basicClass;
		}

		boolean obf = !name.equals(transformedName);
		
		ClassReader reader = new ClassReader(basicClass);
		ClassNode node = new ClassNode(ASM5);
		reader.accept(node, 0);
		
		if(transformedName.equals(STRUCTURE_VILLAGE_PIECES_START)) {
			doStructureVillagePiecesStart(node);
		} else if(transformedName.equals(ENTITY_ZOMBIE_VILLAGER)) {
			doEntityZombieVillager(node, obf);
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
		/*
		byte[] lol = writer.toByteArray();
		CheckClassAdapter.verify(new ClassReader(lol), null, true, new PrintWriter(System.err));
		return lol;
		*/
	}

	private static void doStructureVillagePiecesStart(ClassNode node) {
		done:
		for(MethodNode method : node.methods) {
			//There's 2 constructors and I want the one with lots of parameters.
			if(!method.name.equals("<init>")) continue;
			if(method.desc.equals("()V")) continue;

			ListIterator<AbstractInsnNode> inserator = method.instructions.iterator();

			boolean found50 = false;

			for(AbstractInsnNode insn; inserator.hasNext();) {
				insn = inserator.next();

				if(!found50 && insn instanceof IntInsnNode && ((IntInsnNode) insn).operand == 50) {
					inserator.remove();

					inserator.add(new FieldInsnNode(
							GETSTATIC,
							"quaternary/zombievillagecontrol/Hooks",
							"zombieVillageChance",
							"I"
					));

					found50 = true;
				}

				//isZombieInfested happens to be the only boolean field to write to \_(:D)_/
				if(found50 && insn.getOpcode() == PUTFIELD) {
					//move before it
					inserator.previous();

					//add the flipping thing
					inserator.add(new FieldInsnNode(
							GETSTATIC,
							"quaternary/zombievillagecontrol/Hooks",
							"flipChance",
							"Z"
					));
					inserator.add(new InsnNode(IXOR));

					//All done
					break done;
				}
			}
		}
	}

	private static void doEntityZombieVillager(ClassNode node, boolean obf) {
		String shouldBurnInDay = obf ? "func_190730_o" : "shouldBurnInDay";

		for(MethodNode method : node.methods) {
			//Trying to be polite here...
			if(method.name.equals(shouldBurnInDay)) {
				System.out.println("[Zombie Village Control] Someone already added a shouldBurnInDay method to EntityZombieVillager? Kinda weird, gonna pop out before i break something...");
				return;
			}
		}

		MethodNode newMethod = new MethodNode(ASM5, ACC_PROTECTED, shouldBurnInDay, "()Z", null, null);
		newMethod.instructions.add(new FieldInsnNode(
			GETSTATIC,
			"quaternary/zombievillagecontrol/Hooks",
			"shouldBurnInDay",
			"Z"
		));
		newMethod.instructions.add(new InsnNode(IRETURN));

		node.methods.add(newMethod);
	}
}
