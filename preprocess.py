from pathlib import Path
import shutil
import os
import re


def remove_lines_with_substrings(input_string, substrings_to_remove, exclude_substrings=[]):
    lines = input_string.split('\n')

    filtered_lines = [
        line for line in lines
        if not any(substring in line for substring in substrings_to_remove)
        or (any(substring in line for substring in exclude_substrings)
        and not any(substring in line for substring in important_keywords))
    ]

    result_string = '\n'.join(filtered_lines)
    return result_string


important_keywords = ["gdx", "lwjgl", "lombok", "earlygrey", "transient", "regexodus"]
keywords = [
"SongSource", "SoundListener", "BufferedSoundSource","Statics","regexodus", "gdx", "lwjgl", "lombok", "earlygrey", "Sprite", "ShapeDrawer", "random", "Viewport",
"@Getter", "@AllArgsConstructor", "Array", "FileHandle", "SoundBuffer", ".Utils", "tuningfork"
"FileHandle", "AudioManager", "World", "Body", "Fixture", "Joint", "ShaderProgram", "JsonIgnore",
"ShaderProgram", "Json", "StreamedSoundSource"
]
exclude_keywords = [
"RigidBody", "AudioManager", "DistanceJoint", "RevoluteJoint", "PrismaticJoint", "RopeJoint", "WheelJoint", "WeldJoint", "SpriteMode", "SpriteRenderer"
]
exclude_files = []

input_file = "../core/src/main/java/com/pulsar/api"
output_file = "../api"
template_path = "../TemplateGame/core/src/main/java/com/pulsar/api"

pattern = re.compile(r"\s*(public|private|static|synchronized|final|strictfp)\s*(static|public|private|synchronized|final|strictfp)?\s*(\<T\>)?\s+(?!void)([a-zA-Z][a-zA-Z0-9]*)((\[\])*)\s+[a-zA-Z][a-zA-Z0-9]*\s*\([^)]*\)\s*\{\}\s*", re.MULTILINE)

block_comment_re = re.compile(r"\/\*(.|\s)*?\*\/", re.MULTILINE)
line_comment_re = re.compile(r"(\/\/.*?)(?=\n|$)")
whitespace_re = re.compile(r" {2,}")


def api_preprocess0(root_folder):
    root_path = Path(root_folder)

    for file_path in root_path.rglob('*'):
        if file_path.is_file() and not os.path.basename(file_path) in exclude_files:
            source = open(file_path, mode="r").read()

            res = ""

            level = 0

            idx = 0
            line = 1
            insideString = False
            comment_block = False
            comment_line = False
            last_char = ''
            enum = False
            enum_str = ""
            for char in source:
                #if last_char == "/" and char == "*": comment_block = True
                #if last_char == "/" and char == "/": comment_line = True
                if char == " ":
                    enum_str = ""
                if not (comment_block or comment_line):
                    enum_str += char
                    if "enum" in enum_str:
                        enum = True
                    if char == "\n":
                        idx = 0
                        line += 1
                    idx += 1
                    if char == "\"" and not last_char == "\\":
                        insideString = not insideString
                    if level < 2 or enum:
                        res += char
                    if char == "{" and not insideString:
                        level += 1
                    if char == "}" and not insideString:
                        if level == 2 and not enum:
                            res += "}"
                        enum = False
                        level -= 1
                #if last_char == "*" and char == "/": comment_block = True
                #if char == "\n": comment_line == False
                last_char = char

            res = remove_lines_with_substrings(res, keywords, exclude_keywords)

            res = block_comment_re.sub('', res)
            res = line_comment_re.sub('', res)
            res = whitespace_re.sub(' ', res)

            lines = res.split("\n")

            new_res = ""

            comment_block = False
            last_str = ""

            """for line in lines:
                words = line.split()

                mtch = pattern.match(last_str)
                if str(file_path).endswith("Matrix4.java"):
                    if not mtch: print('"' + line + '"')

                if mtch:
                    last_str = ""
                    type = "null"
                    if mtch.group(5):
                        type = "null"
                    elif mtch.group(4) == "boolean":
                        type = "false"
                    elif mtch.group(4) == "byte":
                        type = "(byte)0"
                    elif mtch.group(4) == "short":
                        type = "(short)0"
                    elif mtch.group(4) == "int":
                        type = "0"
                    elif mtch.group(4) == "float":
                        type = "0.0f"
                    elif mtch.group(4) == "long":
                        type = "0L"
                    elif mtch.group(4) == "double":
                        type = "0.0"
                    line = line.replace("{}", "{return " + type + ";}")
                else:
                    last_str += line
                nl = ""
                if line.strip().endswith("{") or line.strip().endswith("}") or line.strip().endswith(";") or line.strip().endswith("*/"):
                    nl = "\n"
                if "//" in line: nl += "*/"
                new_res += line.replace("//", "/*") + nl"""


            func = pattern.search(res)
            while func:
                type = "null"
                text = func.group()
                if func.group(5):
                    type = "null"
                elif func.group(4) == "boolean":
                    type = "false"
                elif func.group(4) == "byte":
                    type = "(byte)0"
                elif func.group(4) == "short":
                    type = "(short)0"
                elif func.group(4) == "int":
                    type = "0"
                elif func.group(4) == "float":
                    type = "0.0f"
                elif func.group(4) == "long":
                    type = "0L"
                elif func.group(4) == "double":
                    type = "0.0"
                text = text.replace("{}", "{return " + type + ";}")

                nl = ""
                if text.strip().endswith("{") or text.strip().endswith("}") or text.strip().endswith(";") or text.strip().endswith("*/"):
                    nl = "\n"
                if "//" in text: nl += "*/"
                text = text.replace("//", "/*") + nl
                res = res.replace(func.group(), text)

                func = pattern.search(res, pos=func.end())

            res = res.replace("final", "")

            new_path = output_file / file_path.relative_to(input_file)
            #print(new_path)

            if not new_path.exists():
                new_path.parent.mkdir(parents=True, exist_ok=True)

            with open(new_path, mode="w+") as f:
                f.write(res)


api_preprocess0(input_file)


def api_preprocess1(root_folder):
    root_path = Path(root_folder)

    for file_path in root_path.rglob('*'):
        if file_path.is_file() and not os.path.basename(file_path) in exclude_files:
            source = open(file_path, mode="r").read()

            res = ""

            source = source.replace("com.pulsar.audio", "com.game.audio")

            lines = source.split("\n")

            for line in lines:

                l = line

                if (
                    (l.startswith("import com.pulsar.") and not l.startswith("import com.pulsar.api.")) or
                    (l.startswith("import static com.pulsar.") and not l.startswith("import static com.pulsar.api."))
                ):
                    l = l.replace("import com.pulsar.utils.", "import com.game.")
                    l = l.replace("import com.pulsar.", "import com.game.")
                    l = l.replace("import static com.pulsar.", "import static com.game.")

                res += l + "\n"

            res = res.replace("final", "")
            out = template_path / file_path.relative_to(input_file)

            if not out.exists():
                out.parent.mkdir(parents=True, exist_ok=True)

            with open(out, mode="w+") as f:
                f.write(res)


api_preprocess1(input_file)

template_output = "build\\lib\\TemplateGame"
api_final_output = "build\\lib\\api"

if Path(template_output).exists():
    shutil.rmtree(template_output)

if Path(api_final_output).exists():
    shutil.rmtree(api_final_output)

shutil.copytree("../TemplateGame", template_output)

shutil.copytree(output_file, api_final_output)


