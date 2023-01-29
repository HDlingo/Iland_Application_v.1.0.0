### <font size=5 color="#0086C4">#团队协作规范</font>

- **Master（主分支）【环境分支】【只读】【保护分支】**
  - 存放的应该是随时可供在生产环境中部署的代码
  - 生命周期：伴随着整个项目的生命周期，项目结束时结束。
  - 分支命名：master（固定)
  - 该分支，由技术组负责维护，其它人只有拉取权限。
- **Develop（个人开发分支）【Dev】**
  - 个人 branch 命名标准: Dev\_学号/姓名
  - 基于 master 分支派生，是每次迭代版本的共有开发分支
  - 生命周期：一个阶段功能开发开始到本阶段结束
  - 该分支，由开发人员在各自的（feature/hotfix）分支开发完成后，合并至该分支,与 master 主分支合并需经过技术组全体同学获悉测试
- **Test（个人测试分支）【环境分支】【只读】【保护分支】**
  - 个人 branch 命名标准: Dev\_学号/姓名
  - 从 develop 分支派生，由管理员或测试人员操作
  - 生命周期：一个阶段功能开发开始到结束，完成阶段功能测试并修复所有发现 bug，合并会 develop 分支结束
  - 该分支，用于个人 develop 分支基础上进行更改维护，在完成之后可与 Develop 进行合并
- **Protected（个人备份分支）【只读】【保护分支】**
  - 个人 branch 命名标准: Dev\_学号/姓名
  - 用于 Develop 分支的保护，由管理员或测试人员操作
  - 生命周期:当进行 Test 分支合并 Develop 时，用于对原 Develop 分支进行备份

>

**整体分支构成**

> - **master** > &nbsp;&nbsp;&nbsp;&nbsp; ----- Dev_2054310
>   &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ----- Tst_2054310
>   &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ----- Pro_2054310

---

- **项目管理仓库:**
  - **HDlingo_app_v1.0.0**
  - https://github.com/Merakxxx/HDlingo_app_v1.0.0.git
